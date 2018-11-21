#version 330
in vec2 textureCoord;
out vec4 color;
uniform sampler2D diffuse;

void main() {
  color = texture2D(diffuse, textureCoord.st);
}
