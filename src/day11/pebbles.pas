program Pebbles;

{$i bigint48.pas }

type
  FileEntry = record
    Number: BigInt;
    Count:  BigInt;
  end;

  HashEntry = record
    Number: BigInt;
    Offset: Integer;
  end;

const
  MaxTable = 4095;

var
  HashTable: array[0..MaxTable] of HashEntry;
  Big2024: BigInt;

const
  ProgressStr: String[4] = '|/-\';
  ProgressHi: Byte = 1;
  ProgressLo: Byte = 0;

procedure Clear;
var
  I: Integer;
begin
  for I := 0 to MaxTable do
    HashTable[I].Offset := -1;
end;

function Lookup(var Number: BigInt; var Entry: Integer): Boolean;
begin
  Entry := (Number[0] or (Number[1] shl 8)) and MaxTable;
  while True do
  begin
    if HashTable[Entry].Offset = -1 then
    begin
      Lookup := False;
      Exit;
    end;

    if BigCmp(HashTable[Entry].Number, Number) = 0 then
    begin
      Lookup := True;
      Exit;
    end;

    Entry := (Entry + 17) and MaxTable;
  end;
end;

function IntToStr(I: Integer): String;
var
  S: String[30];
begin
  Str(I, S);
  IntToStr := S;
end;

procedure Simulate(Step: Integer; var Total: BigInt);
var
  InFile, OutFile: file of FileEntry;
  Entry: FileEntry;
  Left, Right: BigInt;
  S, T: String[30];
  X, Y, P: Integer;

  procedure AddStones(var Number, Count: BigInt);
  var
    Entry: FileEntry;
    Index: Integer;
  begin
    if Lookup(Number, Index) then
    begin
      Seek(OutFile, HashTable[Index].Offset);
      Read(OutFile, Entry);
      BigAdd(Entry.Count, Count);
      Seek(OutFile, HashTable[Index].Offset);
      Write(OutFile, Entry);
    end
    else
    begin
      HashTable[Index].Number := Number;
      HashTable[Index].Offset := FileSize(OutFile);

      Entry.Number := Number;
      Entry.Count := Count;
      Seek(OutFile, FileSize(OutFile));
      Write(OutFile, Entry);
    end;

    BigAdd(Total, Count);
  end;

begin
  X := 1 + 16 * ((Step - 1) mod 5);
  Y := 3 + (Step - 1) div 5;

  Assign(InFile, 'step-' + IntToStr(Step - 1) + '.tmp');
  Reset(InFile);

  Assign(OutFile, 'step-' + IntToStr(Step) + '.tmp');
  Rewrite(OutFile);

  Clear;
  Total := BigMin;

  while not Eof(InFile) do
  begin
    Inc(ProgressLo);
    if (ProgressLo = 10) then
    begin
      ProgressLo := 0;
      GotoXY(X + 15, Y);
      Write(ProgressStr[ProgressHi]);
      Inc(ProgressHi);
      if ProgressHi > 4 then ProgressHi := 1;
    end;

    Read(InFile, Entry);

    with Entry do
    begin
      BigStr(Number, S);

      if BigCmp(Number, BigMin) = 0 then
        AddStones(BigOne, Count)
      else if BigCmp(Number, BigOne) = 0 then
        AddStones(Big2024, Count)
      else if Odd(Length(S)) then
      begin
        BigMul(Number, Big2024);
        AddStones(Number, Count);
      end
      else
      begin
        P := Length(S) div 2;
        BigVal(Copy(S, 1, P), Left);
        BigVal(Copy(S, P + 1, 255), Right);
        AddStones(Left, Count);
        AddStones(Right, Count);
      end;
    end;
  end;

  Close(OutFile);
  Close(InFile);

  BigStr(Total, S);
  GotoXY(X, Y);
  Write(S:16);
end;

var
  Puzzle: Text;
  IniFile: file of FileEntry;
  Entry: FileEntry;
  Part1, Part2: BigInt;
  S: String[40];
  I: Integer;

begin
  Write(#27'f');

  ClrScr;

  WriteLn('*** AoC 2024.11 Plutonian Pebbles ***');
  WriteLn;

  BigVal('2024', Big2024);

  Assign(Puzzle, ParamStr(1));
  Reset(Puzzle);
  ReadLn(Puzzle, S);
  Close(Puzzle);

  Entry.Count := BigOne;

  Assign(IniFile, 'step-0.tmp');
  Rewrite(IniFile);

  S := S + ' ';
  I := 1;
  while Length(S) <> 0 do
  begin
    I := Pos(' ', S);
    BigVal(Copy(S, 1, I - 1), Entry.Number);
    Write(IniFile, Entry);
    S := Copy(S, I + 1, 255);
  end;

  Close(IniFile);

  for I := 1 to 25 do
    Simulate(I, Part1);

  for I := 26 to 75 do
    Simulate(I, Part2);

  WriteLn;
  WriteLn;

  BigStr(Part1, S);
  WriteLn('Part 1: ', S:15);
  BigStr(Part2, S);
  WriteLn('Part 2: ', S:15);

  Write(#27'e');
end.
