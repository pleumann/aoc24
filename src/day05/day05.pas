program Day05;

{$I /Users/joerg/Projects/pl0/lib/files.pas}

type
  Pair = record
    Left, Right: Integer;
  end;

var
  Rules: array[0..1499] of Pair;
  Count: Integer;
  Pages: array[0..99] of Integer;

function IntToStr(S: String): Integer;
var
  I, J: Integer;
begin
  (*WriteLn('-->', S);*)
  Val(S, I, J);
  IntToStr := I;
end;

procedure ParsePages(S: String);
var
  I, J, K: Integer;
begin
  for I := 0 to 99 do
    Pages[I] := -1;

  I := 1;
  K := 0;
  while I <= Length(S) do
  begin
    J := IntToStr(Copy(S, I, 2)); 
    (*Write(J:3);*)
    Pages[J] := I;
    I := I + 3;
    K := K + 1;
  end;
  (*WriteLn;*)
end;

function CheckPages(var S: String): Boolean;
var
  I, L, R: Integer;
  C: Char;
begin
  CheckPages := True;

  GotoXY(1, 6);
  Write(S);

  for I := 0 to Count - 1 do
  begin
    L := Pages[Rules[I].Left];
    R := Pages[Rules[I].Right];

    (*WriteLn(L, ' ', R);*)

    if (L <> -1) and (R <> - 1) and (L > R) then
    begin
      Pages[Rules[I].Left] := R;
      Pages[Rules[I].Right] := L;

      C := S[L];
      S[L] := S[R];
      S[R] := C;

      C := S[L + 1];
      S[L + 1] := S[R + 1];
      S[R + 1] := C;

      CheckPages := False;
      (*Exit;*)
    end;
  end;
end;

var
  T: Text;
  S, U: String;
  Part1, Part2: Integer;

begin
  Write(#27'f');

  ClrScr;
  WriteLn('*** AoC 2024.05 Print Queue ***');
  WriteLn;
  WriteLn('Part 1:    0');
  WriteLn('Part 2:    0');
 
  Assign(T, 'input.txt');
  Reset(T);

  Count := 0;

  ReadLine(T, S);
  while S <> '' do
  begin
    Rules[Count].Left := IntToStr(Copy(S, 1, 2));
    Rules[Count].Right := IntToStr(Copy(S, 4, 2));
    Count := Count + 1;
    ReadLine(T, S);
  end;

  Part1 := 0;
  Part2 := 0;
    
  while not IsEof(T) do
  begin
    ReadLine(T, S);
    ParsePages(S);

    GotoXY(1, 6);
    Write(#27'L');

    if CheckPages(S) then
    begin
      U := Copy(S, Length(S) / 2, 2);
      Part1 := Part1 + IntToStr(U);
      GotoXY(9, 3);
      Write(Part1:4);
    end
    else
    begin
      repeat
      until CheckPages(S);

      U := Copy(S, Length(S) / 2, 2);
      Part2 := Part2 + IntToStr(U);
      GotoXY(9, 4);
      Write(Part2:4);
    end;

    GotoXY(Length(S) / 2, 6);
    Write(#27'p', Copy(S, Length(S) / 2, 2), #27'q');
  end;

  Close(T);

  GotoXY(1, 23);
  Write(#27'J'#27'e');
end.