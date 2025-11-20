#!/usr/bin/env python3
# Run a command and capture its output into a Python variable.

import subprocess
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
    cdCommand = "cd " + os.getcwd() + "/../../../../../../../../cli/build/native/nativeCompile/"
    fosscutCommand = "./fosscut "
    cmd = []
    cmd.append(cdCommand)
    cmd.append(" && ")
    cmd.append(fosscutCommand)
    cmd.append("optimalgen -iu 1000 -il 500 -it 10 -ol 0.4 -ou 0.8 -oc 10000 --timeout-amount 10 --timeout-unit SECONDS -ot 10 --seed ")
    cmd.append(str(seed))
    cmd.append(" -o lol")
    return "".join(cmd)

def run_fosscut_command():
    good_seeds = []
    for seed in range(1, 1000):
        cmd = get_fosscut_command(seed)
        rc, stdout, stderr = run_command(cmd, use_shell=True, check=False)
        if (rc is None) or (rc != 0):
            print(f"Command failed or timed out for seed {seed}. RC: {rc}")
            continue
        else:
            good_seeds.append(seed)
            print(f"Good seeds: {good_seeds}")
            if len(good_seeds) >= 50:
                break

def main():
    run_fosscut_command()

if __name__ == "__main__":
    main()
