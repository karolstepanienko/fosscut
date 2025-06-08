import { z } from 'zod';

const AirflowDAGLogsDTO = z.object({
  status: z.string() || z.null(),
  logs: z.string()
});

type AirflowDAGLogsDTO = z.infer<typeof AirflowDAGLogsDTO>;

export default AirflowDAGLogsDTO;
