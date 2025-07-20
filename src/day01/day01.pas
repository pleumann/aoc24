program Puzzle;

const
  MaxCount = 1000;

type
  IntArray = array[1..MaxCount] of integer;

var
  left, right: IntArray;
  count, i, j: integer;
  f: text;
  s: string[80];
  spacePos, err, a, b: integer;
  part1, part2: integer;

procedure Sort(var A: IntArray; N: integer);
var
  i, j, temp: integer;
begin
  for i := 1 to N - 1 do
    for j := i + 1 to N do
      if A[i] > A[j] then
      begin
        temp := A[i];
        A[i] := A[j];
        A[j] := temp;
      end;
end;

begin
  writeln;
  writeln('*** AoC 2024.01 Historian Hysteria ***');
  writeln;

  assign(f, 'example.txt');
  reset(f);
  count := 0;
  while not eof(f) do
  begin
    readln(f, s);
    spacePos := pos(' ', s);
    val(copy(s, 1, spacePos - 1), a, err);
    val(copy(s, spacePos + 1, length(s)), b, err);
    count := count + 1;
    left[count] := a;
    right[count] := b;
  end;
  close(f);

  Sort(left, count);
  Sort(right, count);

  part1 := 0;
  part2 := 0;
  j := 1;

  for i := 1 to count do
  begin
    part1 := part1 + abs(right[i] - left[i]);

    if (i = 1) or (left[i] <> left[i - 1]) then
    begin
      while (j <= count) and (right[j] < left[i]) do
        j := j + 1;

      while (j <= count) and (right[j] = left[i]) do
      begin
        part2 := part2 + left[i];
        j := j + 1;
      end;
    end;
  end;

  writeln('Part 1: ', part1);
  writeln('Part 2: ', part2);
end.

