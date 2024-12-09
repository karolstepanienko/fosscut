package com.fosscut.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;

import com.fosscut.alg.cg.ColumnGeneration;
import com.fosscut.exceptions.NotIntegerLPTaskException;
import com.fosscut.type.cutting.plan.CuttingPlan;

public class YamlDumper {
    public String dump(ColumnGeneration columnGeneration) {
        Yaml yaml = new Yaml();

        CuttingPlan cuttingPlan = new CuttingPlan();
        try {
            cuttingPlan = columnGeneration.getCuttingPlan();
        } catch (NotIntegerLPTaskException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        return yaml.dumpAs(cuttingPlan, Tag.MAP, null);
    }
}
