program Day19;

{$a+}

{$I /Users/joerg/Projects/pl0/lib/files.pas}
{$I bigint.pas}

type
  TowelPtr = ^Towel;
  Towel = record
    Pattern: String[10];
    Next: TowelPtr;
  end;

var
  Towels: array['a'..'z'] of TowelPtr;

  Table: array[0..79] of BigInt;
  Used: array[0..79] of Boolean;

  Pattern: String;

procedure Init;
var
  C: Char;
begin
  for C := 'a' to 'z' do
    Towels[C] := nil;
end;

procedure Clear;
var
  I: Integer;
begin
  for I := 0 to 79 do
  begin
    Used[I] := False;
    Table[I] := BigMin;
  end;
end;

procedure AddTowel(S: String);
var
  C: Char;
  P: TowelPtr;
begin
  C := S[1];
  New(P);
  P^.Pattern := S;
  P^.Next := Towels[C];
  Towels[C] := P;
end;

function StrEquals(var A, B: Char; Len: Integer): Boolean; register; inline
(
  $41 /               (*       ld b,c     *)
  $1a /               (* loop: ld a, (de) *)
  $be /               (*       cp (hl)    *)
  $28 / $04 /         (*       jr z,pass  *)
  $21 / $00 / $00 /   (*       ld hl, 0   *)
  $c9 /               (*       ret        *)
  $23 /               (* pass: inc hl     *)
  $13 /               (*       inc de     *)
  $10 / $f4 /         (*       djnz loop  *)
  $21 / $01 / $00 /   (*       ld hl, 1   *)
  $c9                 (*       ret        *)
);


function NumberOfWays(Pattern: String): BigInt;
var
  I, J, LP, LT: Integer;
  C: Char;
  P: TowelPtr;
begin
  for I := 0 to 79 do
    Table[I] := BigMin;

  LP := Byte(Pattern[0]);

  Table[LP + 1] := BigOne;

  for J := LP downto 1 do
  begin
    C := Pattern[J];
    P := Towels[C];
    while P <> nil do
    begin
      LT := Byte(P^.Pattern[0]);
      if J + LT <= LP + 1 then
        if StrEquals(Pattern[J], P^.Pattern[1], LT) then
        begin
          GotoXY(J, 22);
          Write(Copy(Pattern, J, LT));
          BigAdd(Table[J], Table[J + LT]);
          (*GotoXY(J, 22);
          Write(#27'q', Copy(Pattern, J, LT));*)
        end;

      P := P^.Next;
    end;
  end;

  NumberOfWays := Table[1];
end;

procedure Explore(Index: Integer; var Count: BigInt);
var
  I, LT, LP: Integer;
  P: TowelPtr;
begin
  LP := Byte(Pattern[0]);

  if Index = LP + 1 then
  begin
    BigAdd(Count, BigOne);
    Exit;
  end;

  if not Used[Index] then
  begin
    P := Towels[Pattern[Index]];
    while P <> nil do
    begin
      LT := Byte(P^.Pattern[0]);

      if Index + LT <= LP + 1 then
        if StrEquals(Pattern[Index], P^.Pattern[1], LT) then
          Explore(Index + LT, Table[Index]);

      P := P^.Next;
    end;
    
    Used[Index] := True;
  end;

  BigAdd(Count, Table[Index]);
end;

procedure Solve(Path: String);
var
  T: Text;
  S: String;
  C: Char;
  Part1: Integer;
  Part2, Tmp: BigInt;
begin
  Part1 := 0;
  Part2 := BigMin;

  Assign(T, Path);
  Reset(T);

  S := '';
  C := ReadChar(T);
  while C >= ' ' do
  begin
    if C >= 'a' then
      S := S + C
    else if C = ',' then
    begin
      AddTowel(S);
      S := '';
    end;

    C := ReadChar(T);
  end;

  AddTowel(S);

  ReadLine(T, S);
  while not IsEof(T) do
  begin
    ReadLine(T, Pattern);
    GotoXY(1, 6);
    Write(#27'M');
    GotoXY(1, 22);
    Write(#27'p', Pattern, #27'q');
    (*Clear;*)
    Tmp := NumberOfWays(Pattern);
    if BigCmp(Tmp, BigMin) <> 0 then
      Inc(Part1);

    BigStr(Tmp, S);
    GotoXY(60, 22); Write(' -> ', S:15);

    BigAdd(Part2, Tmp);

    GotoXY(9, 3);
    Write(Part1:15);
    GotoXY(9, 4);
    BigStr(Part2, S);
    Write(S: 15);
  end;
end;
    
var
  S, T: String;

begin
  Write(#27'E'#27'f');
  WriteLn('*** AoC 2024.19 Linen Layout ***');
  WriteLn;
  WriteLn('Part 1:');
  WriteLn('Part 2:');
(*
  S := 'HALLO';
  T := 'HAILO';
  WriteLn(StrEquals(S[1], T[1], 5));
*)
  InitHeap(32768);
  Init;
  Solve('INPUT   .TXT');

  GotoXY(1, 23);
  Write(#27'e');
end.

