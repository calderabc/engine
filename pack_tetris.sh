#!/bin/bash

dest_dir=tetris_bin

javapackager -deploy -native -outdir . \
    -srcdir . -srcfiles tetris.jar -appclass engine.puzzle.tetris.TetrisGame \
    -name "$dest_dir" -title "Tetris"

# Haven't figured out how to get javapackager to do this so doing manually.
cp letters.png numbers.png score.png input_to_action.ini level.png piece_image_sheet.png tetris_piece.dat blocks.png lines.png pieces.png next.png puzzle.ini tetris.ini "$dest_dir"/app

