#version 300 es

uniform float animFraction;
out vec4 vertexColor;

void main() {
    float x = 1.8 * animFraction - 0.9;
    vec4 position = vec4(x, 0.0, 0.0, 1.0);

    if (position.x < -0.5) {
        vertexColor = vec4(1.0, 0.0, 0.0, 1.0); // Red
    } else if (position.x >= -0.5 && position.x <= 0.5) {
        vertexColor = vec4(0.0, 1.0, 0.0, 1.0); // Green
    } else {
        vertexColor = vec4(0.0, 0.0, 1.0, 1.0); // Blue
    }

    gl_Position = position;
    gl_PointSize = 100.0;
}