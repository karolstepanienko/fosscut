import "./PatternBar.css";

// @ts-types="npm:@types/react-bootstrap/ProgressBar"
import { ProgressBar } from "npm:react-bootstrap/ProgressBar";

import PlanTableOutputRow from "../../type/PlanTableOutputRow.ts";

const PatternBar = (
    { inputLength, outputs}:
    { inputLength: number, outputs: PlanTableOutputRow[] }) => {

    const getOutputsToRender = (outputs: PlanTableOutputRow[]) => {
      const outputsToRender: PlanTableOutputRow[] = [];
      outputs.forEach((output: PlanTableOutputRow) => {
        for (let i = 0; i < output.count; i++) {
          outputsToRender.push(output);
        }
      });
      return outputsToRender;
    }

    return (
      <div className="progress-bar-container">
        <ProgressBar>
          {getOutputsToRender(outputs).map((output: PlanTableOutputRow, index: number) => (
              <ProgressBar
                className={`output-type-${index % 4} output-text`}
                min={0}
                now={output.length}
                label={`${output.length}`}
                key={index}
                max={inputLength}
              />
            ))}
        </ProgressBar>
      </div>
    );
};

export default PatternBar;
