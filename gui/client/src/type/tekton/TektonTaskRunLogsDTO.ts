import { z } from 'zod';

const TektonTaskRunLogsDTO = z.object({
  status: z.string(),
  reason: z.string(),
  logs: z.string()
});

type TektonTaskRunLogsDTO = z.infer<typeof TektonTaskRunLogsDTO>;

export default TektonTaskRunLogsDTO;
