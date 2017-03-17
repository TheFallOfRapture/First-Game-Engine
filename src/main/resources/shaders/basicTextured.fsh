#version 330
in vec4 fragColor;
in vec2 textureCoord;
out vec4 color;
uniform sampler2D diffuse;
void main() {
  color = fragColor * texture2D(diffuse, textureCoord.st);
}
