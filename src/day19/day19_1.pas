program Day19;

{$a-}

{$I /Users/joerg/Projects/pl0/lib/files.pas}
{$I bigint.pas}

var
  Towels: array[0..499] of String[10];
  NumOfTowels: Integer;

  Results: array[0..79] of BigInt;
  Used: array[0..79] of Boolean;

  Pattern: String;
  LenPattern: Integer;

procedure ClearResults;
var
  I: Integer;
begin
  for I := 0 to 79 do
  begin
    Results[I] := BigMin;
    Used[I] := False;
  end;
end;

function Matches(Pos: Integer; var S: String): Boolean;
var
  Len, I: Integer;
begin
  Matches := False;

  Len := Length(S);
  if Pos + Len > LenPattern + 1 then Exit;

  for I := 0 to Len - 1 do
    if Pattern[Pos + I] <> S[1 + I] then Exit;

  Matches := True;
end;

procedure Explore(Index: Integer; var Count: BigInt);
var
  I: Integer;
begin
  (* WriteLn(Index); *)

  if Index = Length(Pattern) + 1 then
  begin
   (* WriteLn('*****'); *)
    BigAdd(Count, BigOne);
    Exit;
  end;

  if not Used[Index] then
  begin
    for I := 0 to NumOfTowels - 1 do
    begin
      (*Write(Copy(Pattern, Index, Length(Towels[I])), ' vs ', Towels[I], ' -> ');*)
      if Matches(Index, Towels[I]) then
      begin
        (*WriteLn('YES');*)
        Explore(Index + Length(Towels[I]), Results[Index]);
      end;
(*      else
        WriteLn('NO'); *)
    end;
    
    Used[Index] := True;
  end;

  BigAdd(Count, Results[Index]);
end;

procedure Solve(Path: String);
var
  T: Text;
  S: String;
  C: Char;
  Part1, Part2: BigInt;
begin
  Part1 := BigMin;
  Part2 := BigMin;

  Assign(T, Path);
  Reset(T);

  NumOfTowels := 0;

  S := '';
  C := ReadChar(T);
  while C >= ' ' do
  begin
    if C >= 'a' then
      S := S + C
    else if C = ',' then
    begin
      Towels[NumOfTowels] := S;
      Inc(NumOfTowels);
      S := '';
    end;

    C := ReadChar(T);
  end;

  Towels[NumOfTowels] := S;
  Inc(NumOfTowels);

  WriteLn(NumOfTowels, ' towels found.');

  ReadLine(T, S);
  while not IsEof(T) do
  begin
    ReadLine(T, Pattern);
    WriteLn(Pattern);
    LenPattern := Length(Pattern);
    ClearResults;
    Explore(1, Results[0]);
    if Results[0] <> BigMin then
      BigAdd(Part1, BigOne);

    BigAdd(Part2, Results[0]);
    WriteLn;
  end;

  BigStr(Part1, S);
  WriteLn('Part 1: ', S);

  BigStr(Part2, S);
  WriteLn('Part 2: ', S);
end;
    
begin
  Solve('INPUT   .TXT');
end.

