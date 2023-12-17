#version 300 es

layout(location = 0) in vec2 position;

out vec4 vertexColor;

void main() {
    if (position.x < -0.5) {
        vertexColor = vec4(1.0, 0.0, 0.0, 1.0); // Red
    } else if (position.x >= -0.5 && position.x <= 0.5) {
        vertexColor = vec4(0.0, 1.0, 0.0, 1.0); // Green
    } else {
        vertexColor = vec4(0.0, 0.0, 1.0, 1.0); // Blue
    }

    gl_Position = vec4(position, 0.0, 1.0);
    gl_PointSize = 100.0;
}