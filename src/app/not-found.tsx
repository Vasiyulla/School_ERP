import Link from "next/link";
import { Button } from "@/components/ui/button";

export default function NotFound() {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center p-4 text-center font-mono">
      <div className="space-y-2">
        <span className="text-xs font-semibold text-muted-foreground uppercase tracking-widest">
          404 Error
        </span>
        <h2 className="text-xl font-bold tracking-tight text-foreground font-sans">
          Resource Not Found
        </h2>
        <p className="text-xs text-muted-foreground max-w-sm">
          The requested administrative resource or path does not exist in the routing table.
        </p>
      </div>
      <Button asChild size="sm" className="mt-4 h-8 text-xs font-mono">
        <Link href="/admin">Return to Command Center</Link>
      </Button>
    </div>
  );
}

