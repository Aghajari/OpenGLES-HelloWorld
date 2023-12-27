#version 300 es

precision mediump float;

in vec4 vertexColor;

out vec4 FragColor;

void main() {
    vec2 circleCoord = 2.0 * gl_PointCoord - 1.0;
    if (dot(circleCoord, circleCoord) > 1.0) {
        discard;
    }

    FragColor = vertexColor;
}