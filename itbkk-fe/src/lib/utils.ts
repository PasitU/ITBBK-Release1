import { type ClassValue, clsx } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function shortenTitle(title){
  if(title.length > 20){
    return `${title.slice(-20)}...`
  }
  return title
}