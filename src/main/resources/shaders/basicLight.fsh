#version 330
in vec4 fragColor;
in vec2 textureCoord;
in vec3 worldPos;
out vec4 color;

uniform mediump sampler2D diffuse;
uniform mediump sampler2D normal;
uniform vec4 diffuseColor;

uniform struct Light {
    float brightness;
    vec3 color;
    vec3 position;
} lights[10];

void main() {
    vec4 texColor = texture2D(diffuse, textureCoord.st);
    vec4 normal = texture2D(normal, textureCoord.st);

    vec4 diffuse = vec4(0, 0, 0, 1);
    for (int i = 0; i < 10; i++) {
        vec3 toLight = lights[i].position - worldPos;
        vec3 L = normalize(toLight);
        float D = length(toLight);
        float LdotN = dot(L, normalize(normal.xyz));
        float lightFactor = (1.0 / (D * D));
        diffuse += vec4(lights[i].color * lights[i].brightness * lightFactor * LdotN, 0);
    }

    diffuse *= diffuseColor;

    color = fragColor * texColor * diffuse;
}
