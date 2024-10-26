.text
main:
la $a0, A
la $a1, B
lw $a2, n
jal evenElem
exit:
li $v0, 10
syscall
ISODD:            # $v0 = isodd($a0)
move $t0, $a0	  #ob übergebene Zahl ungerade
andi $v0, $t0, 1
jr $ra
ISEVEN:           # $v0 = iseven($a0)
move $t8, $ra     # Speicher Rückkehradresse
jal ISODD
xor $v0, $v0, 1	  # rückgabewert umkehren (wenn 0 dann 1 und wenn 1 dann 0) 
		#-> rückgabewert von ISODD wird invertiert um den von ISEVEN zu erhalten
jr $t8
evenElem:         # $v1 = evenElem($a0 = A[], $a1 = B[], $a2 = n)
li $v1, 0         # j, Zähler für gerade Elemente auf 0
li $t9, 0         # i, Indes Zähler auf 0
move $t6, $a0     # kopiert A in t6 ISEVEN nutzt §a0, nutzen wir $t6
move $t7, $ra     # save return address
#while ($t9 < $s2)
loop:
bge $t9, $a2, loopEnd	# if i > A beende schleife
lw $a0, ($t6)    # $a0 = A[i]
jal ISEVEN
beqz $v0, endIf
lw $t5, ($t6)
sw $t5, ($a1)
addi $a1, $a1, 4  # j++, um 4Bytes erhöhen um zum nächsten element zu gelangen
addi $v1, $v1, 1
endIf:
addi $t6, $t6, 4  # i++, adresse auf nächstes element verschieben
addi $t9, $t9, 1  # nächsten index in A zu repräsentieren
j loop		  # wiederholt bis alle elemente geprüft und in B kopiert
loopEnd:
jr $t7
.data
n:
.word 6
A:
.word 3 4 6 8 11 13
B:
.word 0 0 0 0 0 0    #zielarray
