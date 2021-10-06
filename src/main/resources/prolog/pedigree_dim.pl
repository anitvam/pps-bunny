% commento
max_bunny_size_for_width(W, BP, G, BS) :-
BS is (W + BP) / ((BP + 1) ** (G - 1) - 1).

% commento
max_bunny_size_for_height(H, BI, FI, G, BS) :- 
BS is (H * BI * FI) / ((BI * FI + 1 + FI) * G).

bunny_size_unlimited(H, W, BP, BI, FI, G, BSH) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, FI, G, BSH),
	BSH =< BSW.
	
bunny_size_unlimited(H, W, BP, BI, FI, G, BSW) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, FI, G, BSH),
	BSW < BSH.

bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MaxBS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, PBS),
	PBS > MaxBS.
	
bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, PBS),
	PBS < MinBS.
	
bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, BS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, BS).

max_generations_for_width(W, BP, BS, G) :- 
G is 1 + (log(1 + ((W + BP) / BS)) / log(BP + 1)).

max_generations_for_height(H, BI, FI, BS, G) :- 
G is (H * BI * FI) / ((BI * FI + 1 + FI) * BS).

generations(H, W, BP, BI, FI, BS, GH):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, FI, BS, GH),
	GH =< GW.

generations(H, W, BP, BI, FI, BS, GW):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, FI, BS, GH),
	GW < GH.

pedigree_dimensions(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS, NGC):-
	bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS),
	generations(H, W, BP, BI, FI, MinBS, NG), !, 
	NGC is ceiling(NG).

pedigree_dimensions(H, W, BP, BI, FI, MaxBS, MinBS, G, BSF, G):-
	bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, BS),
	BSF is floor(BS).