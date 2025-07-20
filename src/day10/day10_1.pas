program Day10;

{$I /Users/joerg/Projects/pl0/lib/files.pas}

var
  Map: array[0..44, 0..44] of Char;

  Seen: array[0..44, 0..44] of Boolean;

  Width, Height, Part1, Part2, I, J: Integer;

procedure Load(Path: String);
var
  T: Text;
  S: String;
begin
  Assign(T, Path);
  Reset(T);

  Height := 0;

  while not IsEof(T) do
  begin
    ReadLine(T, S);
    Move(S[1], Map[Height], Length(S));
    Inc(Height);
  end;

  Width := Length(S);
end;

procedure Explore(X, Y: Integer; C: Char);
begin
  if C ='9' then
  begin
    if not Seen[X, Y] then
    begin
      Inc(Part1);
      Seen[X, Y] := True;
    end;

    Inc(Part2);
    Exit;
  end;

  Inc(C);

  if (X > 0)      and (Map[X - 1, Y] = C) then Explore(X - 1, Y, C);
  if (Y > 0)      and (Map[X, Y - 1] = C) then Explore(X, Y - 1, C);
  if (X < Height) and (Map[X + 1, Y] = C) then Explore(X + 1, Y, C);
  if (Y < Width)  and (Map[X, Y + 1] = C) then Explore(X, Y + 1, C);
end;
    
begin
  WriteLn('*** AoC 2024.10 Hoof It ***');

  Load('INPUT   .TXT');

  Part1 := 0;
  Part2 := 0;

  for I := 0 to Height - 1 do
    for J := 0 to Width - 1 do
      if Map[I, J] = '0' then
      begin
        FillChar(Seen, SizeOf(Seen), 0);
        Explore(I, J, '0');
      end;

  WriteLn('Part 1: ', Part1);
  WriteLn('Part 2: ', Part2);
end.
