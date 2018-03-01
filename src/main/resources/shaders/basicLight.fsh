#version 330
in vec4 fragColor;
in vec2 textureCoord;
in vec3 worldPos;
out vec4 color;

uniform sampler2D diffuse;
uniform vec4 diffuseColor;
uniform vec3 lightPosition;

void main() {
    vec4 texColor = texture2D(diffuse, textureCoord.st);
    vec3 toLight = lightPosition - worldPos;
    vec3 L = normalize(toLight);
    float D = length(toLight);

    float lightFactor = (5 / (D * D));

    color = fragColor * texColor * diffuseColor * lightFactor;
}
