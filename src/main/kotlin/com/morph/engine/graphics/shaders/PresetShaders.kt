package com.morph.engine.graphics.shaders

class BasicTexturedShader : Shader<BasicTexturedShaderUniforms>("shaders/basicTextured", BasicTexturedShaderUniforms())
class GUIShader : Shader<GUIShaderUniforms>("shaders/basicTextured", GUIShaderUniforms())
class GUITextShader : Shader<GUITextShaderUniforms>("shaders/text", GUITextShaderUniforms())
class GUITintShader : Shader<GUITintShaderUniforms>("shaders/tint", GUITintShaderUniforms())
class GUITintTransitionShader : Shader<GUITintTransitionShaderUniforms>("shaders/tintTransition", GUITintTransitionShaderUniforms())
class GUITransitionShader : Shader<GUITransitionShaderUniforms>("shaders/transition", GUITransitionShaderUniforms())
class TextShader : Shader<TextShaderUniforms>("shaders/text", TextShaderUniforms())
class TintShader : Shader<TintShaderUniforms>("shaders/tint", TintShaderUniforms())
class TransitionShader : Shader<TransitionShaderUniforms>("shaders/transition", TransitionShaderUniforms())