import subprocess
import sys
import os

# Looks for seeds that generate fosscut optimalgen order that will NOT be solved
# optimally by cg SCIP solver running with 1 thread

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
    return proc.returncode, proc.stdout, proc.stderr

def get_fosscut_command(seed):
    cdCommand = "cd " + os.getcwd() + "/../../../../../../../cli/build/native/nativeCompile/"
    fosscutCommand = "./fosscut "
    cmd = []
    cmd.append(cdCommand)
    cmd.append(" && ")
    cmd.append(fosscutCommand)
    cmd.append("optimalgen -iu 1000 -il 500 -it 10 -ol 0.4 -ou 0.8 -ot 150 -oc 10000 --timeout-amount 10 --timeout-unit SECONDS --seed ")
    cmd.append(str(seed))
    cmd.append(" -o lolAUTO && ")
    cmd.append(fosscutCommand)
    cmd.append("cg --linear-solver CLP --integer-solver SCIP -ln 1 -in 1 --timeout-amount 3 --timeout-unit MINUTES lolAUTO")
    return "".join(cmd)

def run_fosscut_command():
    good_seeds = []
    for seed in range(1, 1000):
        cmd = get_fosscut_command(seed)
        rc, stdout, stderr = run_command(cmd, use_shell=True, check=False)
        if (rc is None) or (rc != 0):
            print(f"Command failed or timed out for seed {seed}. RC: {rc}")
            continue
        if (stdout.find("totalWaste: 0") != -1) and (stdout.find("trueTotalWaste") == -1):
            print(f"Seed {seed} solved optimally, skipping.")
            continue
        else:
            good_seeds.append(seed)
            print(f"Good seeds: {good_seeds}")
        if (len(good_seeds) >= 100):
            break
    print(f"Good seeds: {good_seeds}")

def main():
    run_fosscut_command()

if __name__ == "__main__":
    main()
