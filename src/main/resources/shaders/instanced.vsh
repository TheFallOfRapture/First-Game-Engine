#version 330
layout(location = 0) in vec3 position;
layout(location = 1) in vec4 color;
layout(location = 2) in vec2 texCoord;
layout(location = 3) in mat4 mvp;

out vec4 fragColor;
out vec2 textureCoord;

void main() {
  gl_Position = mvp * vec4(position, 1);
  fragColor = color;
  textureCoord = texCoord;
}
