program Day13;

{$I /Users/joerg/Projects/pl0/lib/files.pas}
{$I bigint.pas}

function GetNumber(S: string; I: Integer; var B: BigInt): Integer;
var
  J: Integer;
begin
  GetNumber := 0;

  while (I <= Length(S)) and ((S[I] < '0') or (S[I] > '9')) do
    Inc(I);

  if I > Length(S) then
    Exit;

  J := I;

  while (I <= Length(S)) and (S[I] >= '0') and (S[I] <= '9') do
    Inc(I);

  (* WriteLn('*** ', Copy(S, J, I - J), ' ***'); *)

  BigVal(Copy(S, J, I - J), B);

  GetNumber := I
end;

function RunCramer(A1, B1, C1, A2, B2, C2: BigInt; var P, Q: BigInt): Boolean;
var
  Det, Tmp, Tmp2, Rem: BigInt;
  S: String;
begin
  (*BigStr(A1, S); WriteLn('A1 : ', S);
  BigStr(B1, S); WriteLn('B1 : ', S);
  BigStr(C1, S); WriteLn('C1 : ', S);
  BigStr(A2, S); WriteLn('A2 : ', S);
  BigStr(B2, S); WriteLn('B2 : ', S);
  BigStr(C2, S); WriteLn('C2 : ', S);*)
  
  RunCramer := False;

  Det := A1;
  BigMul(Det, B2);
  Tmp := A2;
  BigMul(Tmp, B1);
  BigSub(Det, Tmp);

  (*BigStr(Det, S); WriteLn('Det: ', S);*)

  if BigCmp(Det, BigMin) = 0 then
    Exit;

  P := C1;
  BigMul(P, B2);
  Tmp := C2;
  BigMul(Tmp, B1);
  BigSub(P, Tmp);
  BigDiv(P, Det, Rem);

  Q := A1;
  BigMul(Q, C2);
  Tmp := A2;
  BigMul(Tmp, C1);
  BigSub(Q, Tmp);
  BigDiv(Q, Det, Rem);

  (*BigStr(P, S); WriteLn('P  : ', S);
  BigStr(Q, S); WriteLn('Q  : ', S);*)

  Tmp := P;
  BigMul(Tmp, A1);
  Tmp2 := Q;
  BigMul(Tmp2, B1);
  BigAdd(Tmp, Tmp2);

  if BigCmp(Tmp, C1) <> 0 then
    Exit;

  Tmp := P;
  BigMul(Tmp, A2);
  Tmp2 := Q;
  BigMul(Tmp2, B2);
  BigAdd(Tmp, Tmp2);

  if BigCmp(Tmp, C2) <> 0 then
    Exit;

  RunCramer := True;
end;

procedure Solve(Path: String);
var
  T: Text;
  S1, S2, S3, S4: String;
  I, H, V: Integer;
  Trillion, Three, A1, A2, B1, B2, C1, C2, P, Q, Cost, Part1, Part2: BigInt;
  Ok: Boolean;
begin
  BigVal('10000000000000', Trillion);
  BigVal('3', Three);
  Part1 := BigMin;
  Part2 := BigMin;

  H := 0;
  V := 0;

  Assign(T, Path);
  Reset(T);

  while not IsEof(T) do
  begin
    ReadLine(T, S1);
    ReadLine(T, S2);
    ReadLine(T, S3);
    ReadLine(T, S4);

    GotoXY(1, 3);

    WriteLn(S1, '   ');
    WriteLn(S2, '   ');
    WriteLn(S3, '   ');
    WriteLn(S4, '   ');
   
    I := GetNumber(S1, 1, A1);
    I := GetNumber(S1, I, A2);

    I := GetNumber(S2, 1, B1);
    I := GetNumber(S2, I, B2);

    I := GetNumber(S3, 1, C1);
    I := GetNumber(S3, I, C2);

    if RunCramer(A1, B1, C1, A2, B2, C2, P, Q) then
    begin
      Cost := P;
      BigMul(Cost, Three);
      BigAdd(Cost, Q);
      BigAdd(Part1, Cost);

      Ok := True;
    end
    else if RunCramer(A2, B2, C2, A1, B1, C1, P, Q) then
    begin
      Cost := P;
      BigMul(Cost, Three);
      BigAdd(Cost, Q);
      BigAdd(Part1, Cost);

      Ok := True;
    end
    else
      Ok := False;

    GotoXY(4 + H, 8 + V);
    if Ok then
    begin
      Write(#27'pX', #27'q');

      BigStr(Part1, S1);
      GotoXY(1, 19);
      WriteLn('Part 1: ', S1:15);
    end
    else
      Write(#27'p ', #27'q');

    BigAdd(C1, Trillion);
    BigAdd(C2, Trillion);

    if RunCramer(A1, B1, C1, A2, B2, C2, P, Q) then
    begin
      Cost := P;
      BigMul(Cost, Three);
      BigAdd(Cost, Q);
      BigAdd(Part2, Cost);

      Ok := True;
    end
    else if RunCramer(A2, B2, C2, A1, B1, C1, P, Q) then
    begin
      Cost := P;
      BigMul(Cost, Three);
      BigAdd(Cost, Q);
      BigAdd(Part2, Cost);

      Ok := True;
    end
    else
      Ok := False;

    GotoXY(44 + H, 8 + V);
    if Ok then
    begin
      Write(#27'pX', #27'q');

      BigStr(Part2, S1);
      GotoXY(1, 20);
      WriteLn('Part 2: ', S1:15);
    end
    else
      Write(#27'p ', #27'q');

    Inc(H);
    if H = 32 then
    begin
      Inc(V);
      H := 0;
    end;
  end;
end;
    
begin
  Write(#27'f');

  ClrScr;
  WriteLn('*** AoC 2024.13 Claw Contraption ***');

  GotoXY(4, 7);
  (*Write('----------- Part 1 ------------        ------------ Part 2 ------------');*)
  Write('-------- Prizes part 1 ---------        -------- Prizes part 2 ---------');

  Solve('INPUT   .TXT');

  Write(#27'f');
end.

