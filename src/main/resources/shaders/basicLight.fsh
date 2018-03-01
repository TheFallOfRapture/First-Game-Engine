#version 330
in vec4 fragColor;
in vec2 textureCoord;
out vec4 color;

uniform sampler2D diffuse;
uniform vec4 diffuseColor;
uniform vec2 lightPosition;
uniform mat4 mvp;

void main() {
    vec4 texColor = texture2D(diffuse, textureCoord.st);
    vec2 lightScreenPos = (mvp * vec4(lightPosition, 0, 1)).xy;
    vec2 fragToLight = gl_FragCoord.xy - lightScreenPos;
    float lightDistance = length(fragToLight);

    float lightFactor = 500000.0 / (lightDistance * lightDistance);

    color = fragColor * texColor * diffuseColor * lightFactor;
}
