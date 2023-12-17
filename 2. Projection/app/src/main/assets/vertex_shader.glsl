#version 300 es

layout(location = 0) in vec2 position;
uniform mat4 projection;
out vec4 vertexColor;

void main() {
    vec4 rhsPosition = projection * vec4(position, 0.0, 1.0);

    if (rhsPosition.x < -0.5) {
        vertexColor = vec4(1.0, 0.0, 0.0, 1.0); // Red
    } else if (rhsPosition.x >= -0.5 && rhsPosition.x <= 0.5) {
        vertexColor = vec4(0.0, 1.0, 0.0, 1.0); // Green
    } else {
        vertexColor = vec4(0.0, 0.0, 1.0, 1.0); // Blue
    }

    gl_Position = rhsPosition;
    gl_PointSize = 100.0;
}