package app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.devices.Workforce;
import model.material.MaterialType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import simulation.SimulationBuilder;

import java.io.IOException;
import java.util.List;

@Component
public class JsonSimulatorLoader {
    private record JsonConfig(List<String> repairmen, List<JsonLine> lines) {}
    private record JsonLine(String name, String outputType, List<JsonWorkforce> workforce) {}
    private record JsonWorkforce(String type, String model) {}

    public void loadConfig(SimulationBuilder builder, String fileName) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonConfig config = mapper.readValue(
                    new ClassPathResource(fileName).getFile(),JsonConfig.class);

            if(config.repairmen != null) {
                config.repairmen.forEach(builder::addRepairTech);
            }

            if(config.lines != null) {
                for(JsonLine line : config.lines) {
                    List<Workforce> workforces = line.workforce.stream()
                            .map(w-> createWorkforce(builder, w))
                            .toList();

                    builder.addProductionLine(line.name, workforces, MaterialType.valueOf(line.outputType));
                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Workforce createWorkforce(SimulationBuilder builder, JsonWorkforce workforce) {
        if(workforce.type.equalsIgnoreCase("WORKER")) {
            return builder.createWorker(workforce.model);
        }else if(workforce.type.equalsIgnoreCase("MACHINE")) {
            return builder.createMachine(workforce.model);
        }else if(workforce.type.equalsIgnoreCase("ROBOT")) {
            return builder.createRobot(workforce.model);
        }else{
            throw new RuntimeException("Unknown workforce type: " + workforce.type);
        }
    }

}
