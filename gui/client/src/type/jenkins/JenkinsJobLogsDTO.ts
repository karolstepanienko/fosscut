import { z } from 'zod';

const JenkinsJobLogsDTO = z.object({
  httpStatusCode: z.number(),
  jobNumberIdentifier: z.number(),
  status: z.string().nullable(),
  building: z.string().nullable(),
  result: z.string().nullable(),
  logs: z.string().nullable()
});

type JenkinsJobLogsDTO = z.infer<typeof JenkinsJobLogsDTO>;

export default JenkinsJobLogsDTO;
