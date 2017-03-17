#version 330
in vec4 fragColor;
in vec2 textureCoord;
out vec4 color;
uniform sampler2D diff1;
uniform sampler2D diff2;
uniform vec4 diffuseColor;
uniform float lerpFactor;

void main() {
  color = fragColor * diffuseColor * ((texture2D(diff1, textureCoord.st) * (1.0 - lerpFactor)) + (texture2D(diff2, textureCoord.st) * lerpFactor));
}
