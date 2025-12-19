package app.controller;

import app.dto.FactoryStateReport;
import app.report.FilePrinter;
import app.service.SimulationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;
    private final FilePrinter filePrinter;

    public SimulationController(SimulationService simulationService, FilePrinter filePrinter) {
        this.simulationService = simulationService;
        this.filePrinter = filePrinter;
    }

    @GetMapping("/tick")
    public String tick() {
        return simulationService.tick();
    }
    @GetMapping("/state")
    public FactoryStateReport state() {
        FactoryStateReport report = simulationService.getFactoryState();
        filePrinter.saveReports(report);
        return report;
    }

    @GetMapping(value = "/events/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamEvents() {
        return simulationService.createEventStream();
    }
}