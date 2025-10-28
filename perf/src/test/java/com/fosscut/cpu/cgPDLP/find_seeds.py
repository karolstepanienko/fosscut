#!/usr/bin/env python3
# Run a command and capture its output into a Python variable.

import subprocess
import sys
import os

def run_command(cmd, use_shell=False, check=False):
    """
    Run a command and return (returncode, stdout, stderr).
    - cmd: list (recommended) or string (if use_shell=True).
    - use_shell: whether to run via the shell.
    - check: if True, raise CalledProcessError on non-zero exit.
    - if the command times out (30s), return (None, "", "").
    """
    try:
        proc = subprocess.run(cmd, shell=use_shell, capture_output=True, text=True, timeout=30)
    except subprocess.TimeoutExpired:
        return None, "", ""
    if check and proc.returncode != 0:
        raise subprocess.CalledProcessError(proc.returncode, cmd, output=proc.stdout, stderr=proc.stderr)
    return proc.returncode, proc.stdout, proc.stderr

def get_fosscut_command(seed):
    cdCommand = "cd " + os.getcwd() + "/../../../../../../../cli/build/native/nativeCompile/"
    fosscutCommand = "./fosscut "
    cmd = []
    cmd.append(cdCommand)
    cmd.append(" && ")
    cmd.append(fosscutCommand)
    cmd.append("optimalgen -iu 1000 -il 100 -it 5 -ol 0.4 -ou 0.8 -oc 1000 -ot 30 --seed ")
    cmd.append(str(seed))
    cmd.append(" -o lol && ")
    cmd.append(fosscutCommand)
    cmd.append("cg --linear-solver PDLP --integer-solver SCIP -ln 1 -in 1 lol")
    return "".join(cmd)

def run_fosscut_command():
    good_seeds = []
    for seed in range(559, 1000):
        cmd = get_fosscut_command(seed)
        rc, stdout, stderr = run_command(cmd, use_shell=True, check=False)
        if (rc is None) or (rc != 0):
            print(f"Command failed or timed out for seed {seed}. RC: {rc}")
            continue
        for line in stdout.splitlines():
            if "totalWaste: " in line:
                total_waste = int(line.split("totalWaste: ")[1])
                if total_waste > 10 and total_waste < 50:
                    good_seeds.append(seed)
                    print(f"Good seeds: {good_seeds}")
    print(f"Good seeds: {good_seeds}")
def main():
    run_fosscut_command()

if __name__ == "__main__":
    main()
