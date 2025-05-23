import json
import re

def tokenize_text(text, tokenizer_json):
    MAX_LEN = 50

    tokenizer_data = json.loads(tokenizer_json)
    word_index = tokenizer_data.get("word_index", {})

    tokens = re.findall(r"\b\w+\b", text.lower())
    sequence = [word_index.get(word, 0) for word in tokens]

    if len(sequence) < MAX_LEN:
        sequence += [0] * (MAX_LEN - len(sequence))
    else:
        sequence = sequence[:MAX_LEN]

    return sequence