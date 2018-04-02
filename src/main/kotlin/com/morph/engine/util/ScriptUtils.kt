package com.morph.engine.util

import com.morph.engine.core.Game
import com.morph.engine.entities.Entity
import com.morph.engine.graphics.LoadedFont
import com.morph.engine.script.ConsoleScript
import com.morph.engine.script.EntityBehavior
import com.morph.engine.script.GameBehavior
import com.morph.engine.script.ScriptContainer
import com.morph.engine.script.debug.Console
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import org.jetbrains.kotlin.script.jsr223.KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory
import java.io.IOException
import java.nio.file.*
import java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY
import java.nio.file.StandardWatchEventKinds.OVERFLOW
import java.util.*
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException
import javax.script.SimpleBindings

/**
 * Created on 7/5/2017.
 */
object ScriptUtils {
    private val supportedScriptEngines = HashMap<String, ScriptEngine>()
    private val scriptedEntities = HashMap<String, MutableList<Entity>>()
    private val bindings = SimpleBindings()
    private var isRunning: Boolean = false
    var isInitialized: Boolean = false
        private set
    var initializationTask: Completable? = null
        private set
    private val closeRequests = PublishSubject.create<Boolean>()

    private val SCRIPT_ENGINE_EXTENSIONS = listOf("kts")

    fun init(game: Game) {
        initializationTask = Single.just(game).flatMapCompletable { g -> Completable.fromCallable { ScriptUtils.load(g) } }.cache()

        val loadedScripts = initializationTask?.subscribeOn(Schedulers.io())
                ?.andThen<WatchEvent<Path>>(Observable.create { runReactive(it) })
                ?.map { event -> Paths.get("scripts/").resolve(event.context()).fileName.toString() }
                ?.flatMapMaybe { getScriptBehaviorAsync(it) }

        loadedScripts
                ?.filter { EntityBehavior::class.java.isInstance(it) }
                ?.subscribe { behavior -> scriptedEntities[behavior.name]?.forEach { entity -> entity.getComponent(ScriptContainer::class.java)?.replaceBehavior(behavior.name!!, behavior as EntityBehavior) } }

        loadedScripts
                ?.filter { behavior -> !EntityBehavior::class.java.isInstance(behavior) }
                ?.subscribe { behavior -> game.replaceBehavior(behavior.name, behavior) }

        //                .subscribe(behavior -> {
        //                    if (behavior instanceof EntityBehavior) scriptedEntities.get(behavior.getName()).forEach(entity -> entity.getComponent(ScriptContainer.class).replaceBehavior(behavior.getName(), (EntityBehavior)behavior));
        //                    else game.replaceBehavior(behavior.getName(), behavior);
        //                });
    }

    private fun load(game: Game): Boolean {
        Console.out.println("Morph Script Engine " + Game.VERSION_STRING + " initializing... Please wait...")
        val kotlinEngine = KotlinJsr223JvmDaemonLocalEvalScriptEngineFactory()
        //        PyScriptEngineFactory pythonEngine = new PyScriptEngineFactory();

        val manager = ScriptEngineManager()
        manager.registerEngineExtension("kts", kotlinEngine)
        //        manager.registerEngineExtension("py", pythonEngine);

        supportedScriptEngines["kts"] = kotlinEngine.scriptEngine
        //        supportedScriptEngines.put("py", pythonEngine.getScriptEngine());

        println(Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/Test.kts").fileName)
        //        System.out.println("Python support test: " + supportedScriptEngines.get("py").getFactory().getLanguageName());

        isInitialized = true

        Console.out.println("[Morph Script Engine " + Game.VERSION_STRING + "] Engine initialized.")

        return true
    }

    private fun runReactive(subscriber: ObservableEmitter<WatchEvent<Path>>) {
        closeRequests.subscribe { if (it) isRunning = false }

        isRunning = true

        try {
            FileSystems.getDefault().newWatchService().use { watchService ->
                Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY)
                while (isRunning) {
                    val key = watchService.poll()
                    key?.let {
                        it.pollEvents().forEach { e ->
                            e?.let {
                                val kind = it.kind()

                                if (kind === OVERFLOW) return@forEach

                                (it as? WatchEvent<Path>)?.let { subscriber.onNext(it) }
                            }
                        }

                        it.reset()
                    }
                }
                subscriber.onComplete()
            }
        } catch (e: IOException) {
            subscriber.onError(e)
        }

    }

    @Deprecated("")
    private fun run(game: Game) {
        try {
            FileSystems.getDefault().newWatchService().use { watchService ->
                val key = Paths.get(System.getProperty("user.dir") + "/src/main/resources/scripts/").register(watchService, ENTRY_MODIFY)
                while (isRunning) {
                    pollEvents(watchService, game)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Deprecated("")
    private fun start(game: Game) {
        if (isRunning) return

        isRunning = true
        run(game)
    }

    fun stop() {
        if (!isRunning) return

        closeRequests.onNext(true)
    }

    @Deprecated("")
    fun pollEvents(watchService: WatchService, game: Game) {
        val key = watchService.poll()
        if (key != null) {
            for (e in key.pollEvents()) {
                val kind = e.kind()

                if (kind === OVERFLOW) continue

                val event = e as WatchEvent<Path>
                val filename = event.context()

                val file = Paths.get("scripts/").resolve(filename)
                val simpleName = file.fileName.toString()

                println(simpleName)
                val newBehavior = getScriptBehavior<GameBehavior>(simpleName)

                if (newBehavior is EntityBehavior)
                    scriptedEntities[simpleName]?.forEach { entity -> entity.getComponent(ScriptContainer::class.java)!!.replaceBehavior(simpleName, (newBehavior as EntityBehavior?)!!) }
                else
                    game.replaceBehavior(simpleName, newBehavior)
            }

            key.reset()
        }
    }

    fun register(scriptName: String, e: Entity) {
        if (scriptedEntities[scriptName] == null) {
            val eList = ArrayList<Entity>()
            eList.add(e)
            scriptedEntities[scriptName] = eList
        } else {
            scriptedEntities[scriptName]?.add(e)
        }
    }

    private fun genScript(script: String): String {
        return "import com.morph.engine.script.ConsoleScript\n" +
                "class CustomScript : ConsoleScript() {\n" +
                "    override fun run() = " + script + "\n" +
                "}\n" +
                "CustomScript()"
    }

    fun readScriptAsync(script: String, lang: String, console: Console): Completable {
        return getScriptEngine(lang).flatMapCompletable { engine ->
            Completable.fromCallable {
                readScriptDI(script, engine, console)
            }
        }
    }

    @Throws(ScriptException::class)
    private fun readScriptDI(script: String, engine: ScriptEngine, console: Console) {
        val sanitizedScript = script.filter { LoadedFont.isValidChar(it) }
        val behavior = engine.eval(genScript(sanitizedScript), bindings) as ConsoleScript
        behavior.setConsole(console)
        behavior.run()

        bindings.clear()
    }

    @Throws(ScriptException::class)
    fun readScriptBlocking(script: String, lang: String, console: Console) {
        val engine = getScriptEngine(lang).blockingGet()
        readScriptDI(script, engine, console)
    }

    // TODO: When initialization is finished, return all requested script behaviors.
    private fun getScriptEngine(lang: String): Maybe<ScriptEngine> {
        return initializationTask!!.andThen(resolveScriptEngine(lang))
    }

    private fun resolveScriptEngine(lang: String): Maybe<ScriptEngine> {
        return if (supportedScriptEngines.containsKey(lang)) {
            Maybe.fromCallable { supportedScriptEngines[lang] }
        } else {
            Maybe.empty()
        }
    }

    fun getScriptBehaviorAsync(filename: String): Maybe<GameBehavior> {
        return getScriptEngine(getFileExtension(filename)).observeOn(Schedulers.single()).flatMap { scriptEngine -> Maybe.just(getScriptBehaviorDI<GameBehavior>(filename, scriptEngine)!!) }
    }

    private fun <T : GameBehavior> getScriptBehaviorDI(filename: String, engine: ScriptEngine): T? {
        var scriptSource = ""
        val fullFilename = System.getProperty("user.dir") + "/src/main/resources/scripts/" + filename

        val extension = fullFilename.substring(fullFilename.indexOf(".") + 1)

        try {
            scriptSource = IOUtils.getFileAsStringAbsolute(fullFilename)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        var behavior: T? = null

        try {
            behavior = engine.eval(scriptSource, bindings) as T
            if (behavior == null) {
                println("No result from eval, getting script variable")
                behavior = bindings["script"] as T
            }
            behavior.name = filename
        } catch (e: ScriptException) {
            e.printStackTrace()
        }

        bindings.clear()

        return behavior
    }

    private fun getFileExtension(filename: String): String {
        val fullFilename = System.getProperty("user.dir") + "/src/main/resources/scripts/" + filename
        return fullFilename.substring(fullFilename.indexOf(".") + 1)
    }

    @Deprecated("")
    fun <T : GameBehavior> getScriptBehavior(filename: String): T? {
        val extension = getFileExtension(filename)
        val engine = getScriptEngine(extension).blockingGet()

        return getScriptBehaviorDI(filename, engine)
    }

    fun isSupported(lang: String): Boolean {
        return SCRIPT_ENGINE_EXTENSIONS.contains(lang)
    }
}
