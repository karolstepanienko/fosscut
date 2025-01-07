import { useMemo } from "react";
import {
  MantineReactTable,
  useMantineReactTable,
  type MRT_ColumnDef,
  type MRT_Row
} from "mantine-react-table";
import PlanTableRow from "../type/PlanTableRow.ts";
import PlanDetailsTable from "./PlanDetailsTable.tsx";

type PlanTableProps = {
  planTableData: PlanTableRow[]
}

const PlanTable: React.FC<PlanTableProps> = ({planTableData}) => {
  const columns = useMemo<MRT_ColumnDef<PlanTableRow>[]>(
    () => [
      {
        accessorKey: 'patternId',
        header: 'Pattern ID',
        mantineTableHeadCellProps: {
          align: 'center',
        },
      },
      {
        accessorKey: 'inputLength',
        header: 'Input length',
        mantineTableHeadCellProps: {
          align: 'center',
        },
      },
      {
        accessorKey: 'count',
        header: 'Count',
        mantineTableHeadCellProps: {
          align: 'center',
        },
      },
    ],
    [],
  );

  type RowProps = {
    row: MRT_Row<PlanTableRow>
  }

  const renderPatternDetails = (props: RowProps) => {
    return (
      <div>
        <PlanDetailsTable outputs={props.row.original.outputs}/>
      </div>
    );
  }

  const table = useMantineReactTable<PlanTableRow>({
    columns,
    data: planTableData,
    enableRowActions: true,
    renderDetailPanel: renderPatternDetails
  });

  return (
    <MantineReactTable table={table} />
  );
}

export default PlanTable;
