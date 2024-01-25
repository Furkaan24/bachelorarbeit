import * as React from "react";

function Qr(props) {
  return (
    <svg
      viewBox="0 0 512 512"
      fill="currentColor"
      height="1em"
      width="1em"
      {...props}
    >
      <path d="M344 336 H408 A8 8 0 0 1 416 344 V408 A8 8 0 0 1 408 416 H344 A8 8 0 0 1 336 408 V344 A8 8 0 0 1 344 336 z" />
      <path d="M280 272 H328 A8 8 0 0 1 336 280 V328 A8 8 0 0 1 328 336 H280 A8 8 0 0 1 272 328 V280 A8 8 0 0 1 280 272 z" />
      <path d="M424 416 H472 A8 8 0 0 1 480 424 V472 A8 8 0 0 1 472 480 H424 A8 8 0 0 1 416 472 V424 A8 8 0 0 1 424 416 z" />
      <path d="M440 272 H472 A8 8 0 0 1 480 280 V312 A8 8 0 0 1 472 320 H440 A8 8 0 0 1 432 312 V280 A8 8 0 0 1 440 272 z" />
      <path d="M280 432 H312 A8 8 0 0 1 320 440 V472 A8 8 0 0 1 312 480 H280 A8 8 0 0 1 272 472 V440 A8 8 0 0 1 280 432 z" />
      <path d="M344 96 H408 A8 8 0 0 1 416 104 V168 A8 8 0 0 1 408 176 H344 A8 8 0 0 1 336 168 V104 A8 8 0 0 1 344 96 z" />
      <path
        fill="none"
        stroke="currentColor"
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={32}
        d="M304 48 H448 A16 16 0 0 1 464 64 V208 A16 16 0 0 1 448 224 H304 A16 16 0 0 1 288 208 V64 A16 16 0 0 1 304 48 z"
      />
      <path d="M104 96 H168 A8 8 0 0 1 176 104 V168 A8 8 0 0 1 168 176 H104 A8 8 0 0 1 96 168 V104 A8 8 0 0 1 104 96 z" />
      <path
        fill="none"
        stroke="currentColor"
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={32}
        d="M64 48 H208 A16 16 0 0 1 224 64 V208 A16 16 0 0 1 208 224 H64 A16 16 0 0 1 48 208 V64 A16 16 0 0 1 64 48 z"
      />
      <path d="M104 336 H168 A8 8 0 0 1 176 344 V408 A8 8 0 0 1 168 416 H104 A8 8 0 0 1 96 408 V344 A8 8 0 0 1 104 336 z" />
      <path
        fill="none"
        stroke="currentColor"
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={32}
        d="M64 288 H208 A16 16 0 0 1 224 304 V448 A16 16 0 0 1 208 464 H64 A16 16 0 0 1 48 448 V304 A16 16 0 0 1 64 288 z"
      />
    </svg>
  );
}

export default Qr;