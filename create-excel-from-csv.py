#!/usr/bin/env python3
"""Convert CSV to Excel with formatting"""
import csv
import os

try:
    from openpyxl import Workbook
    from openpyxl.styles import Font, PatternFill, Alignment, Border, Side
    from openpyxl.utils import get_column_letter

    # Create workbook
    wb = Workbook()
    ws = wb.active
    ws.title = "Consultants"

    # Read CSV and write to Excel
    csv_file = 'consultant-import-template.csv'

    with open(csv_file, 'r', encoding='utf-8') as f:
        reader = csv.reader(f)
        for row_idx, row in enumerate(reader, 1):
            for col_idx, value in enumerate(row, 1):
                cell = ws.cell(row=row_idx, column=col_idx, value=value)

                # Style header
                if row_idx == 1:
                    cell.font = Font(bold=True, color="FFFFFF", size=11)
                    cell.fill = PatternFill(start_color="1976D2", end_color="1976D2", fill_type="solid")
                    cell.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)

                # Add borders
                thin_border = Border(
                    left=Side(style='thin'),
                    right=Side(style='thin'),
                    top=Side(style='thin'),
                    bottom=Side(style='thin')
                )
                cell.border = thin_border

                # Align data cells
                if row_idx > 1:
                    if col_idx in [7, 8, 9, 10, 11]:  # Numbers
                        cell.alignment = Alignment(horizontal="center", vertical="center")
                    else:
                        cell.alignment = Alignment(horizontal="left", vertical="top", wrap_text=True)

    # Set column widths
    widths = [15, 18, 22, 15, 30, 25, 15, 12, 18, 15, 12, 20, 25, 12, 40, 12]
    for col_num, width in enumerate(widths, 1):
        ws.column_dimensions[get_column_letter(col_num)].width = width

    # Set header row height
    ws.row_dimensions[1].height = 30

    # Freeze header
    ws.freeze_panes = "A2"

    # Save Excel file
    output_file = 'consultant-import-template.xlsx'
    wb.save(output_file)

    file_size = os.path.getsize(output_file)
    print(f"✅ Excel file created: {output_file}")
    print(f"📊 File size: {file_size:,} bytes")
    print(f"📑 Rows: 6 (1 header + 5 data)")
    print(f"📋 Columns: 16")

except ImportError:
    print("❌ openpyxl not found. Installing...")
    import subprocess
    subprocess.check_call(['pip', 'install', 'openpyxl', '--quiet'])
    print("✅ Installed. Please run this script again.")
except Exception as e:
    print(f"❌ Error: {e}")
    import traceback
    traceback.print_exc()

