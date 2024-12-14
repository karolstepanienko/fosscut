{{/*
Expand the name of the chart.
*/}}
{{- define "fosscut.name" -}}
{{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "fosscut.fullname" -}}
{{- if .Values.fullnameOverride }}
{{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- $name := default .Chart.Name .Values.nameOverride }}
{{- if contains $name .Release.Name }}
{{- .Release.Name | trunc 63 | trimSuffix "-" }}
{{- else }}
{{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
{{- end }}
{{- end }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "fosscut.chart" -}}
{{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "fosscut.labels" -}}
helm.sh/chart: {{ include "fosscut.chart" . }}
{{ include "fosscut.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Common labels with a parameter
*/}}
{{- define "fosscut.labels.param" -}}
helm.sh/chart: {{ include "fosscut.chart" .Context }}
{{ include "fosscut.selectorLabels.param" (dict "Context" .Context "Name" .Name) }}
{{- if .Context.Chart.AppVersion }}
app.kubernetes.io/version: {{ .Context.Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Context.Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "fosscut.selectorLabels" -}}
app.kubernetes.io/name: {{ include "fosscut.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Selector labels with a parameter
*/}}
{{- define "fosscut.selectorLabels.param" -}}
app.kubernetes.io/name: {{ include "fosscut.name" .Context }}-{{ .Name }}
app.kubernetes.io/instance: {{ .Context.Release.Name }}-{{ .Name }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "fosscut.serviceAccountName" -}}
{{- if .Values.serviceAccount.create }}
{{- default (include "fosscut.fullname" .) .Values.serviceAccount.name }}
{{- else }}
{{- default "default" .Values.serviceAccount.name }}
{{- end }}
{{- end }}

{{- define "strToBool" -}}
    {{- $output := "" -}}
    {{- if or (eq . "true") (eq . "yes") (eq . "on") -}}
        {{- $output = "1" -}}
    {{- end -}}
    {{ $output }}
{{- end -}}