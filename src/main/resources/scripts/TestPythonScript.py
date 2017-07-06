from com.morph.engine.script import EntityBehavior

class TestPythonScript(EntityBehavior):
    def init(self):
        print "Hello, we've got Python support in Morph 0.5.13!"
    def start(self):
        print "This Python script has been restarted!"

script = TestPythonScript()
