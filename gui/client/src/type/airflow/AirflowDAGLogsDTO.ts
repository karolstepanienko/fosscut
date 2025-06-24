import { z } from 'zod';

const AirflowDAGLogsDTO = z.object({
  status: z.string().nullable(),
  logs: z.string()
});

type AirflowDAGLogsDTO = z.infer<typeof AirflowDAGLogsDTO>;

export default AirflowDAGLogsDTO;
