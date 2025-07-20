program Day04;

{$I /Users/joerg/Projects/pl0/lib/files.pas}

type
  TLine = String[140];

var
  Lines: array[0..139, 0..139] of Char;
  Width, Height, Part1, Part2, OffX, OffY, I, J: Integer;

procedure Load(Path: string);
var
  T: Text;
  S: String;
begin
  Assign(T, Path);
  Reset(T);

  Height := 0;
  Width := 0;

  while not IsEof(T) do
  begin
    ReadLine(T, S);
    Move(S[1], Lines[Height], Length(S));
    Inc(Height);
  end;

  Width := Length(S);
end;

procedure ConOut(C: Char); register;
inline (
  $4d /                 (* ld c,l           *)
  $2a / $01 / $00 /     (* ld hl,($0001)    *)
  $11 / $09 / $00 /     (* ld de, $0009     *)
  $19 /                 (* add hl,de        *)
  $e9                   (* jp (hl)          *)
);

procedure Draw(X, Y, DX, DY: Integer; S: string);
var
  I: Integer;
begin
  Write(#27'p');

  X := X - OffX;
  Y := Y - OffY;

  for I := 1 to Length(S) do
  begin
    if (X < 0) or (Y < 0) or (X >= 20) or (Y >= 70) then Break;

    GotoXY(6 + Y, 3 + X);
    ConOut(S[I]);

    X := X + DX;
    Y := Y + DY;
  end;

  Write(#27'q');
end;

procedure Extract(X, Y, DX, DY, Count: Integer; var S: string);
var
  I: Integer;
begin
  S := '';

  for I := 1 to Count do
  begin
    if (X < 0) or (Y < 0) or (X >= Height) or (Y >= Width) then Break;

    S := S + Lines[X][Y];

    X := X + DX;
    Y := Y + DY;
  end;
end;

procedure Solve1(MinX, MinY, MaxX, MaxY: Integer);
var
  X, Y, DX, DY: Integer;
  S: String;
begin
  for X := MinX to MaxX - 1 do
    for Y := MinY to MaxY - 1 do
      for DX := -1 to 1 do
        for DY := -1 to 1 do
          if (DX <> 0) or (DY <> 0) then
            if Lines[X, Y] = 'X' then
            begin
              Extract(X, Y, DX, DY, 4, S);
              if S = 'XMAS' then
              begin
                Inc(Part1);

                GotoXY(32, 24);
                Write(Part1: 5);

                Draw(X, Y, DX, DY, S);
              end;
            end;
end;

procedure Solve2(MinX, MinY, MaxX, MaxY: Integer);
var
  X, Y: Integer;
  S, T: String;
begin     
  for X := MinX to MaxX - 1 do
    for Y := MinY to MaxY - 1 do
      if Lines[X, Y] = 'A' then
      begin
        Extract(X - 1, Y - 1,  1, 1, 3, S);
        Extract(X + 1, Y - 1, -1, 1, 3, T);

        if ((S = 'MAS') or (S = 'SAM')) and ((T = 'MAS') or (T = 'SAM')) then
        begin
          Inc(Part2);

          GotoXY(51, 24);
          Write(Part2: 5);

          Draw(X - 1, Y - 1,  1, 1, S);
          Draw(X + 1, Y - 1, -1, 1, T);
        end;
      end;
end;

begin
  Write(#27'f');

  ClrScr;
  GotoXY(24, 1);
  WriteLn('*** AoC 2024.04 Ceres Search ***');

  GotoXY(24, 24); Write('Part 1:     0      Part 2:     0');

  Load('input.txt');

  Part1 := 0;
  Part2 := 0;

  OffX := 0;

  while OffX < Height do
  begin
    OffY := 0;

    while OffY < Width do
    begin
      for I := 0 to 19 do
        for J := 0 to 69 do
        begin
          GotoXY(6 + J, 3 + I);
          ConOut(Lines[OffX + I, OffY + J]);
        end;

      Solve1(OffX, OffY, OffX + 20, OffY + 70);

      for I := 0 to 19 do
        for J := 0 to 69 do
        begin
          GotoXY(6 + J, 3 + I);
          ConOut(Lines[OffX + I, OffY + J]);
        end;

      Solve2(OffX, OffY, OffX + 20, OffY + 70);

      OffY := OffY + 70;
    end;

    OffX := OffX + 20;
  end;

  GotoXY(1, 23);
  Write(#27'e');
end.