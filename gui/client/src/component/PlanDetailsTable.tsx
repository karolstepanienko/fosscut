import { useMemo } from 'react';
import {
  MantineReactTable,
  useMantineReactTable,
  type MRT_ColumnDef,
} from 'mantine-react-table';
import PlanTableOutputRow from "../type/PlanTableOutputRow.ts";

type PlanOutputTableProps = {
  outputs: PlanTableOutputRow[]
}

const PlanDetailsTable: React.FC<PlanOutputTableProps> = ({outputs}) => {
  const columns = useMemo<MRT_ColumnDef<PlanTableOutputRow>[]>(
    () => [
      {
        accessorKey: 'id',
        header: 'ID',
        mantineTableHeadCellProps: {
          align: 'center',
        },
      },
      {
        accessorKey: 'length',
        header: 'Output length',
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

  const table = useMantineReactTable<PlanTableOutputRow>({
    columns,
    data: outputs,
    enableRowActions: false
  });

  return (
    <MantineReactTable table={table} />
  );
}

export default PlanDetailsTable;
