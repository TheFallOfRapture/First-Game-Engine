#version 330
in vec4 fragColor;
in vec2 textureCoord;
out vec4 color;
uniform sampler2D diffuse;
uniform vec3 diffuseColor;

void main() {
    vec4 texColor = texture2D(diffuse, textureCoord.st);

    vec3 negative = vec3(1.0) - diffuseColor;

    color = vec4(1, 1, 1, texColor.a) * vec4(diffuseColor, 1);
}
