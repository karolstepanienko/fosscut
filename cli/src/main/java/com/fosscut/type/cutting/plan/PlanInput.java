package com.fosscut.type.cutting.plan;

import java.util.List;

import com.fosscut.type.cutting.Element;

public class PlanInput extends Element {
    private List<Pattern> patterns;

    public List<Pattern> getPatterns() {
        return patterns;
    }

    public void setPatterns(List<Pattern> patterns) {
        this.patterns = patterns;
    }
}
