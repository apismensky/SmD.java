SmD.java
========

Represents dates as 3-bytes in msgpack. We assume dates after 1970, and reasonably close to 'now'.
Their best use is for fairly short-lived expiration dates (months), which don't require incredible precision (nearest hour or nearest minute is fine).
These constraints yield a compression of around 66%, which is fantastic for the intended uses.
