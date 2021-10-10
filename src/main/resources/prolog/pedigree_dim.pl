% max_bunny_size_for_width(+Width, +BunnyPlusProportion, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width.
max_bunny_size_for_width(W, BP, G, BS) :-
BS is (W + BP) / ((BP + 1) ** (G - 1) - 1).

% max_bunny_size_for_height(+Height, +BunnyInfoProportion, +FontInfoPercent, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the height.
max_bunny_size_for_height(H, BI, FI, G, BS) :-
BS is (H * BI * FI * 2) / (((BI * FI *2) + 1 + FI) * G).

% bunny_size_unlimited (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width and the height without limitations for the bunny size.
bunny_size_unlimited(H, W, BP, BI, FI, G, BSH) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, FI, G, BSH),
	BSH =< BSW.
bunny_size_unlimited(H, W, BP, BI, FI, G, BSW) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, FI, G, BSH),
	BSW < BSH.

% bunny_size (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +MaxBunnySize, MinBunnySize, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width and the height with min and max limitations for the bunny size.
bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MaxBS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, PBS),
	PBS > MaxBS.
bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, PBS),
	PBS < MinBS.
bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, BS):-
	bunny_size_unlimited(H, W, BP, BI, FI, G, BS).

% max_generations_for_width(+Width, +BunnyPlusProportion, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the width with a specific bunny size.
max_generations_for_width(W, BP, BS, G) :- 
G is 1 + (log(1 + ((W + BP) / BS)) / log(BP + 1)).

% max_generations_for_height(+Height, +BunnyInfoProportion, +FontInfoPercent, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the height with a specific bunny size.
max_generations_for_height(H, BI, FI, BS, G) :- 
G is (H * BI * FI * 2) / (((BI * FI *2) + 1 + FI) * BS).

% generations (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the width and the height with a specific bunny size.
generations(H, W, BP, BI, FI, BS, GH):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, FI, BS, GH),
	GH =< GW.
generations(H, W, BP, BI, FI, BS, GW):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, FI, BS, GH),
	GW < GH.

% bunny_size (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +MaxBunnySize, MinBunnySize, -BunnySize, -Generations)
% Calculates the bunny size and the generations that can fit in the width and the height trying to maximize both with respect of min and max limitations in bunny size.
pedigree_dimensions(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS, NGC):-
	bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, MinBS),
	generations(H, W, BP, BI, FI, MinBS, NG), !, 
	NGC is ceiling(NG).
pedigree_dimensions(H, W, BP, BI, FI, MaxBS, MinBS, G, BSF, G):-
	bunny_size(H, W, BP, BI, FI, MaxBS, MinBS, G, BS),
	BSF is floor(BS).
