program Day10;

{$I /Users/joerg/Projects/pl0/lib/files.pas}

var
  Map: array[0..44, 0..44] of Char;
  Seen: array[0..44, 0..44] of Boolean;
  Width, Height, Part1, Part2, I, J, K, L, Left: Integer;

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

procedure Explore(TrailX, TrailY, X, Y: Integer; C: Char);
var
  Old: Integer;
begin
  if Map[X, Y] <> C then Exit;

  GotoXY(Left + 10 + Y - TrailY, 13 + X - TrailX);
  Write(#27'p', Map[X, Y], #27'q');

  if C ='9' then
  begin
    if not Seen[X, Y] then
    begin
      Inc(Part1);
      Seen[X, Y] := True;
    end;

    Inc(Part2);
  end
  else
  begin
    Inc(C);

    Old := Part2;

    if (X > 0)      then Explore(TrailX, TrailY, X - 1, Y, C);
    if (Y > 0)      then Explore(TrailX, TrailY, X, Y - 1, C);
    if (X < Height) then Explore(TrailX, TrailY, X + 1, Y, C);
    if (Y < Width)  then Explore(TrailX, TrailY, X, Y + 1, C);

    if Part2 = Old then
    begin
      GotoXY(Left + 10 + Y - TrailY, 13 + X - TrailX);
      Write('.'); (* Map[X, Y]); *)
    end;
  end;
end;
    
begin
  Write(#27'f');

  ClrScr;
  WriteLn('*** AoC 2024.10 Hoof It ***');

  Load('INPUT   .TXT');

  Part1 := 0;
  Part2 := 0;

  Left := 0;

  FillChar(Seen, SizeOf(Seen), 0);

  for I := 0 to Height - 1 do
  begin
    for J := 0 to Width - 1 do
      if Map[I, J] = '0' then
      begin
        GotoXY(1 + Left, 3);
        Write(#27'pTrailhead at ', I:2, ', ', J:2, #27'q');
        for K := -9 to 9 do
        begin
          GotoXY(1 + Left, 13 + K);
          Write('...................');
        end;

        Explore(I, J, I, J, '0');
        FillChar(Seen, SizeOf(Seen), 0);

        Left := (Left + 20) mod 80;

        GotoXY(50, 24);
        Write('Part 1: ', Part1:4, '      Part 2: ', Part2:4);
        GotoXY(1, 23);
      end;
  end;

  Write(#27'f');
end.
