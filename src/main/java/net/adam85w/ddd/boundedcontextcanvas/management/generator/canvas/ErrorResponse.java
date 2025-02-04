package net.adam85w.ddd.boundedcontextcanvas.management.generator.canvas;

import java.time.LocalDateTime;

record ErrorResponse(String appName, String appVersion, String brand, String path, String message, LocalDateTime time) {
}
