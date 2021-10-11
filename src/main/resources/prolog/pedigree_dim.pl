% max_bunny_size_for_width(+Width, +BunnyPlusProportion, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width.
max_bunny_size_for_width(W, BP, G, BS) :-
BS is (W + BP) / ((BP + 1) ** (G - 1) - 1).

% max_bunny_size_for_height(+Height, +BunnyInfoProportion, +FontInfoPercent, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the height.
max_bunny_size_for_height(H, BI, BF, G, BS) :-
BS is (H * BI * BF) / ((BF * BI + 2 * BI + BF) * G).

% bunny_size_unlimited (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width and the height without limitations for the bunny size.
bunny_size_unlimited(H, W, BP, BI, BF, G, BSH) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, BF, G, BSH),
	BSH =< BSW.
bunny_size_unlimited(H, W, BP, BI, BF, G, BSW) :-
	max_bunny_size_for_width(W, BP, G, BSW),
	max_bunny_size_for_height(H, BI, BF, G, BSH),
	BSW < BSH.

% bunny_size (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +MaxBunnySize, MinBunnySize, +Generations, -BunnySize)
% Calculates the maximum bunny size to fit in the width and the height with min and max limitations for the bunny size.
bunny_size(H, W, BP, BI, BF, MaxBS, MinBS, G, MaxBS):-
	bunny_size_unlimited(H, W, BP, BI, BF, G, PBS),
	PBS > MaxBS.
bunny_size(H, W, BP, BI, BF, MaxBS, MinBS, G, MinBS):-
	bunny_size_unlimited(H, W, BP, BI, BF, G, PBS),
	PBS < MinBS.
bunny_size(H, W, BP, BI, BF, MaxBS, MinBS, G, BS):-
	bunny_size_unlimited(H, W, BP, BI, BF, G, BS).

% max_generations_for_width(+Width, +BunnyPlusProportion, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the width with a specific bunny size.
max_generations_for_width(W, BP, BS, G) :- 
G is 1 + (log(1 + ((W + BP) / BS)) / log(BP + 1)).

% max_generations_for_height(+Height, +BunnyInfoProportion, +FontInfoPercent, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the height with a specific bunny size.
max_generations_for_height(H, BI, BF, BS, G) :-
G is (H * BI * BF) / ((BF * BI + 2 * BI + BF) * BS).

% generations (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +BunnySize, -Generations)
% Calculates the maximum generations that can fit in the width and the height with a specific bunny size.
generations(H, W, BP, BI, BF, BS, GH):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, BF, BS, GH),
	GH =< GW.
generations(H, W, BP, BI, BF, BS, GW):-
	max_generations_for_width(W, BP, BS, GW),
	max_generations_for_height(H, BI, BF, BS, GH),
	GW < GH.

% bunny_size (+Height, +Width, +BunnyPlusProportion, +BunnyInfoProportion, +FontInfoPercent, +MaxBunnySize, MinBunnySize, -BunnySize, -Generations)
% Calculates the bunny size and the generations that can fit in the width and the height trying to maximize both with respect of min and max limitations in bunny size.
pedigree_dimensions(H, W, BP, BI, BF, MaxBS, MinBS, G, MinBS, NGC):-
	bunny_size(H, W, BP, BI, BF, MaxBS, MinBS, G, MinBS),
	generations(H, W, BP, BI, BF, MinBS, NG), !,
	NGC is ceiling(NG).
pedigree_dimensions(H, W, BP, BI, BF, MaxBS, MinBS, G, BSF, G):-
	bunny_size(H, W, BP, BI, BF, MaxBS, MinBS, G, BS),
	BSF is floor(BS).
