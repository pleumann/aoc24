program Day04;

{$I /Users/joerg/Projects/pl0/lib/files.pas}

type
  TLine = String[140];

var
  Lines: array[0..139, 0..139] of Char;
  Width, Height: Integer;

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

  WriteLn('Read ', Height, ' lines.');
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

function Solve1: Integer;
var
  Result, X, Y, DX, DY: Integer;
  S: String;
begin
  Result := 0;

  for X := 0 to Height - 1 do
    for Y := 0 to Width - 1 do
      for DX := -1 to 1 do
        for DY := -1 to 1 do
          if (DX <> 0) or (DY <> 0) then
            if Lines[X, Y] = 'X' then
            begin
              Extract(X, Y, DX, DY, 4, S);
              if S = 'XMAS' then
                Inc(Result);
            end;

  Solve1 := Result;
end;

function Solve2: Integer;
var
  Result, X, Y: Integer;
  S, T: String;
begin     
  Result := 0;
   
  for X := 1 to Height - 2 do
    for Y := 1 to Width - 2 do
      if Lines[X, Y] = 'A' then
      begin
        Extract(X - 1, Y - 1,  1, 1, 3, S);
        Extract(X + 1, Y - 1, -1, 1, 3, T);

        if ((S = 'MAS') or (S = 'SAM')) and ((T = 'MAS') or (T = 'SAM')) then
          Inc(Result);
      end;

  Solve2 := Result;
end;

begin
  ClrScr;
  WriteLn('*** AoC 2024.04 Ceres Search ***');

  Load('input.txt');

  WriteLn(Solve1);
  WriteLn(Solve2);
end.