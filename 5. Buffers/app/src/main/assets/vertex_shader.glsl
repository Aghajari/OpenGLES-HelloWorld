#version 300 es

layout(location = 0) in float inY;
layout(location = 1) in vec4 inColor;

uniform float animFraction;
out vec4 vertexColor;

void main() {
    float x = 1.8 * animFraction - 0.9;
    vec4 position = vec4(x, inY, 0.0, 1.0);

    vertexColor = inColor;
    gl_Position = position;
    gl_PointSize = 100.0;
}