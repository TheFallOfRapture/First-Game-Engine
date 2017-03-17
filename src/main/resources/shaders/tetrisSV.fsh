#version 330
in vec4 fragColor;
in vec2 textureCoord;
out vec4 color;
uniform sampler2D diffuse;
uniform vec3 diffuseColor;

void main() {
    vec4 texColor = texture2D(diffuse, textureCoord.st);

    float saturation = texColor.r;
    float value = texColor.g;

    vec3 negative = vec3(1.0) - diffuseColor;

    vec3 colorRGB = (diffuseColor + (negative * (1 - saturation))) * value;

    color = vec4(colorRGB, 1.0);
}
