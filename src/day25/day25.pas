program Day19;

{$a+}

{$I /Users/joerg/Projects/pl0/lib/files.pas}

type
  SchematicPtr = ^Schematic;
  Schematic = record
    Heights: array[0..4] of Byte;
    Next: SchematicPtr;
  end;

var
  Locks, Keys: SchematicPtr;

procedure Load(Path: String);
var
  T: Text;
  S: String;
  P: SchematicPtr;
  I, J, KC, LC: Byte;
  IsLock: Boolean;
begin
  Locks := nil;
  Keys := nil;

  KC := 0;
  LC := 0;

  Assign(T, Path);
  Reset(T);

  while not IsEof(T) do
  begin
    ReadLine(T, S);
    IsLock := S = '#####';

    New(P);
    for I := 0 to 4 do
      P^.Heights[I] := 0;

    for I := 0 to 4 do
    begin
      ReadLine(T, S);
      for J := 0 to 4 do
        if S[J +  1] = '#' then 
          Inc(P^.Heights[J]);
    end;

    (*for I := 0 to 4 do
      Write(P^.Heights[I], ' ');
    WriteLn;*)

    ReadLine(T, S);
    ReadLine(T, S);

    if IsLock then
    begin
      P^.Next := Locks;
      Locks := P;
      Inc(LC);
      GotoXY(9, 4); Write(LC);
    end
    else
    begin
      P^.Next := Keys;
      Keys := P;
      Inc(KC);
      GotoXY(9, 3); Write(KC);
    end;
  end;

  Close(T);
end;

function Fits(L, K: SchematicPtr): Boolean;
var
  I: Integer;
begin
  Fits := False;

  for I := 0 to 4 do
    if L^.Heights[I] + K^.Heights[I] > 5 then
      Exit;

  Fits := True;
end;

procedure ShowLock(L: SchematicPtr);
var
  I, J: Integer;
begin
  for I := 0 to 4 do
  begin
    GotoXY(20, 10 + I);
    for J := 0 to L^.Heights[I] do
      Write('#');
    for J := L^.Heights[I] + 1 to 7 do
      Write(' ');
  end;
end;

procedure ShowKey(L: SchematicPtr);
var
  I, J: Integer;
begin
  for I := 0 to 4 do
  begin
    GotoXY(30, 10 + I);
    for J := L^.Heights[I] + 1 to 7 do
      Write(' ');
    for J := 0 to L^.Heights[I] do
      Write('#');
  end;
end;

function Solve: Integer;
var
  Result, KKK: Integer;
  L, K: SchematicPtr;
begin
  Result := 0;

  L := Locks;
  while L <> nil do
  begin
    ShowLock(L);
    KKK := 0;

    K := Keys;
    while K <> nil do
    begin
      ShowKey(K);
      if Fits(L, K) then
      begin
        Inc(Result);
        Inc(KKK);
      end;

      K := K^.Next;
    end;

    (*WriteLn(KKK);*)
  
    L := L^.Next;
  end;

  Solve := Result;
end;

var
  Part1: Integer;

begin
  (* ClrScr; *)

  WriteLn('*** AoC 2024.25 Code Chronicle ***');
  WriteLn;
  WriteLn('Keys:  ');
  WriteLn('Locks: ');
  WriteLn;
  WriteLn('Part 1:');

  InitHeap(49152);

  Load('INPUT   .TXT');
  Part1 := Solve;

  WriteLn('Part 1: ', Part1);
end.


