enum Backend {
    TEKTON = "Tekton",
    AIRFLOW = "Airflow",
    JENKINS = "Jenkins",
}

type SetBackendFunction = (backend: string) => void;

export type { SetBackendFunction };

export default Backend;
