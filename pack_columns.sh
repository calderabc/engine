#!/bin/bash

dest_dir=columns_bin

javapackager -deploy -native -outdir . \
    -srcdir . -srcfiles columns.jar -appclass engine.puzzle.columns.ColumnsGame \
    -name "$dest_dir" -title "Columns"

# Haven't figured out how to get javapackager to do this so doing manually.
cp columns.ini letters.png numbers.png score.png columns.png input_to_action.ini level.png blocks.png lines.png pieces.png next.png puzzle.ini "$dest_dir"/app

