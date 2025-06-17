import yaml from 'yaml';

export const objectToUrl = (obj: object): string => {
  const yamlContent = yaml.stringify(obj);
  const cuttingPlanBlob = new Blob([yamlContent], { type: 'application/x-yaml' });
  return URL.createObjectURL(cuttingPlanBlob)
}
